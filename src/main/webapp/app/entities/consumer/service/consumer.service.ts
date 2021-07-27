import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IConsumer, getConsumerIdentifier } from '../consumer.model';

export type EntityResponseType = HttpResponse<IConsumer>;
export type EntityArrayResponseType = HttpResponse<IConsumer[]>;

@Injectable({ providedIn: 'root' })
export class ConsumerService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/consumers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(consumer: IConsumer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(consumer);
    return this.http
      .post<IConsumer>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(consumer: IConsumer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(consumer);
    return this.http
      .put<IConsumer>(`${this.resourceUrl}/${getConsumerIdentifier(consumer) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(consumer: IConsumer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(consumer);
    return this.http
      .patch<IConsumer>(`${this.resourceUrl}/${getConsumerIdentifier(consumer) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IConsumer>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IConsumer[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addConsumerToCollectionIfMissing(consumerCollection: IConsumer[], ...consumersToCheck: (IConsumer | null | undefined)[]): IConsumer[] {
    const consumers: IConsumer[] = consumersToCheck.filter(isPresent);
    if (consumers.length > 0) {
      const consumerCollectionIdentifiers = consumerCollection.map(consumerItem => getConsumerIdentifier(consumerItem)!);
      const consumersToAdd = consumers.filter(consumerItem => {
        const consumerIdentifier = getConsumerIdentifier(consumerItem);
        if (consumerIdentifier == null || consumerCollectionIdentifiers.includes(consumerIdentifier)) {
          return false;
        }
        consumerCollectionIdentifiers.push(consumerIdentifier);
        return true;
      });
      return [...consumersToAdd, ...consumerCollection];
    }
    return consumerCollection;
  }

  protected convertDateFromClient(consumer: IConsumer): IConsumer {
    return Object.assign({}, consumer, {
      createdDate: consumer.createdDate?.isValid() ? consumer.createdDate.toJSON() : undefined,
      lastModifiedDate: consumer.lastModifiedDate?.isValid() ? consumer.lastModifiedDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdDate = res.body.createdDate ? dayjs(res.body.createdDate) : undefined;
      res.body.lastModifiedDate = res.body.lastModifiedDate ? dayjs(res.body.lastModifiedDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((consumer: IConsumer) => {
        consumer.createdDate = consumer.createdDate ? dayjs(consumer.createdDate) : undefined;
        consumer.lastModifiedDate = consumer.lastModifiedDate ? dayjs(consumer.lastModifiedDate) : undefined;
      });
    }
    return res;
  }
}
