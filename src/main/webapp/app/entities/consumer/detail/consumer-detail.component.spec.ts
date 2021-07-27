import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ConsumerDetailComponent } from './consumer-detail.component';

describe('Component Tests', () => {
  describe('Consumer Management Detail Component', () => {
    let comp: ConsumerDetailComponent;
    let fixture: ComponentFixture<ConsumerDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ConsumerDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ consumer: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ConsumerDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ConsumerDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load consumer on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.consumer).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
