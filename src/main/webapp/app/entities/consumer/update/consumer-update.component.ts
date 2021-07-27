import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IConsumer, Consumer } from '../consumer.model';
import { ConsumerService } from '../service/consumer.service';

@Component({
  selector: 'jhi-consumer-update',
  templateUrl: './consumer-update.component.html',
})
export class ConsumerUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    guid: [null, [Validators.required, Validators.maxLength(38)]],
    note: [null, [Validators.maxLength(250)]],
    createdBy: [null, [Validators.maxLength(50)]],
    createdDate: [],
    lastModifiedBy: [null, [Validators.maxLength(50)]],
    lastModifiedDate: [],
    recordStatusId: [null, [Validators.required]],
  });

  constructor(protected consumerService: ConsumerService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ consumer }) => {
      if (consumer.id === undefined) {
        const today = dayjs().startOf('day');
        consumer.createdDate = today;
        consumer.lastModifiedDate = today;
      }

      this.updateForm(consumer);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const consumer = this.createFromForm();
    if (consumer.id !== undefined) {
      this.subscribeToSaveResponse(this.consumerService.update(consumer));
    } else {
      this.subscribeToSaveResponse(this.consumerService.create(consumer));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConsumer>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(consumer: IConsumer): void {
    this.editForm.patchValue({
      id: consumer.id,
      guid: consumer.guid,
      note: consumer.note,
      createdBy: consumer.createdBy,
      createdDate: consumer.createdDate ? consumer.createdDate.format(DATE_TIME_FORMAT) : null,
      lastModifiedBy: consumer.lastModifiedBy,
      lastModifiedDate: consumer.lastModifiedDate ? consumer.lastModifiedDate.format(DATE_TIME_FORMAT) : null,
      recordStatusId: consumer.recordStatusId,
    });
  }

  protected createFromForm(): IConsumer {
    return {
      ...new Consumer(),
      id: this.editForm.get(['id'])!.value,
      guid: this.editForm.get(['guid'])!.value,
      note: this.editForm.get(['note'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      createdDate: this.editForm.get(['createdDate'])!.value
        ? dayjs(this.editForm.get(['createdDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      lastModifiedBy: this.editForm.get(['lastModifiedBy'])!.value,
      lastModifiedDate: this.editForm.get(['lastModifiedDate'])!.value
        ? dayjs(this.editForm.get(['lastModifiedDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      recordStatusId: this.editForm.get(['recordStatusId'])!.value,
    };
  }
}
