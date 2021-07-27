jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MessageService } from '../service/message.service';
import { IMessage, Message } from '../message.model';

import { MessageUpdateComponent } from './message-update.component';

describe('Component Tests', () => {
  describe('Message Management Update Component', () => {
    let comp: MessageUpdateComponent;
    let fixture: ComponentFixture<MessageUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let messageService: MessageService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MessageUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(MessageUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MessageUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      messageService = TestBed.inject(MessageService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const message: IMessage = { id: 456 };

        activatedRoute.data = of({ message });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(message));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Message>>();
        const message = { id: 123 };
        jest.spyOn(messageService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ message });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: message }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(messageService.update).toHaveBeenCalledWith(message);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Message>>();
        const message = new Message();
        jest.spyOn(messageService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ message });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: message }));
        saveSubject.complete();

        // THEN
        expect(messageService.create).toHaveBeenCalledWith(message);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Message>>();
        const message = { id: 123 };
        jest.spyOn(messageService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ message });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(messageService.update).toHaveBeenCalledWith(message);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
