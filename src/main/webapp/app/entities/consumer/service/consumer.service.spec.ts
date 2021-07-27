import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IConsumer, Consumer } from '../consumer.model';

import { ConsumerService } from './consumer.service';

describe('Service Tests', () => {
  describe('Consumer Service', () => {
    let service: ConsumerService;
    let httpMock: HttpTestingController;
    let elemDefault: IConsumer;
    let expectedResult: IConsumer | IConsumer[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ConsumerService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        guid: 'AAAAAAA',
        note: 'AAAAAAA',
        createdBy: 'AAAAAAA',
        createdDate: currentDate,
        lastModifiedBy: 'AAAAAAA',
        lastModifiedDate: currentDate,
        recordStatusId: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            createdDate: currentDate.format(DATE_TIME_FORMAT),
            lastModifiedDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Consumer', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            createdDate: currentDate.format(DATE_TIME_FORMAT),
            lastModifiedDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdDate: currentDate,
            lastModifiedDate: currentDate,
          },
          returnedFromService
        );

        service.create(new Consumer()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Consumer', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            guid: 'BBBBBB',
            note: 'BBBBBB',
            createdBy: 'BBBBBB',
            createdDate: currentDate.format(DATE_TIME_FORMAT),
            lastModifiedBy: 'BBBBBB',
            lastModifiedDate: currentDate.format(DATE_TIME_FORMAT),
            recordStatusId: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdDate: currentDate,
            lastModifiedDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Consumer', () => {
        const patchObject = Object.assign(
          {
            note: 'BBBBBB',
            createdBy: 'BBBBBB',
            createdDate: currentDate.format(DATE_TIME_FORMAT),
            lastModifiedBy: 'BBBBBB',
            recordStatusId: 1,
          },
          new Consumer()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            createdDate: currentDate,
            lastModifiedDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Consumer', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            guid: 'BBBBBB',
            note: 'BBBBBB',
            createdBy: 'BBBBBB',
            createdDate: currentDate.format(DATE_TIME_FORMAT),
            lastModifiedBy: 'BBBBBB',
            lastModifiedDate: currentDate.format(DATE_TIME_FORMAT),
            recordStatusId: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdDate: currentDate,
            lastModifiedDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Consumer', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addConsumerToCollectionIfMissing', () => {
        it('should add a Consumer to an empty array', () => {
          const consumer: IConsumer = { id: 123 };
          expectedResult = service.addConsumerToCollectionIfMissing([], consumer);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(consumer);
        });

        it('should not add a Consumer to an array that contains it', () => {
          const consumer: IConsumer = { id: 123 };
          const consumerCollection: IConsumer[] = [
            {
              ...consumer,
            },
            { id: 456 },
          ];
          expectedResult = service.addConsumerToCollectionIfMissing(consumerCollection, consumer);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Consumer to an array that doesn't contain it", () => {
          const consumer: IConsumer = { id: 123 };
          const consumerCollection: IConsumer[] = [{ id: 456 }];
          expectedResult = service.addConsumerToCollectionIfMissing(consumerCollection, consumer);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(consumer);
        });

        it('should add only unique Consumer to an array', () => {
          const consumerArray: IConsumer[] = [{ id: 123 }, { id: 456 }, { id: 4977 }];
          const consumerCollection: IConsumer[] = [{ id: 123 }];
          expectedResult = service.addConsumerToCollectionIfMissing(consumerCollection, ...consumerArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const consumer: IConsumer = { id: 123 };
          const consumer2: IConsumer = { id: 456 };
          expectedResult = service.addConsumerToCollectionIfMissing([], consumer, consumer2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(consumer);
          expect(expectedResult).toContain(consumer2);
        });

        it('should accept null and undefined values', () => {
          const consumer: IConsumer = { id: 123 };
          expectedResult = service.addConsumerToCollectionIfMissing([], null, consumer, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(consumer);
        });

        it('should return initial array if no Consumer is added', () => {
          const consumerCollection: IConsumer[] = [{ id: 123 }];
          expectedResult = service.addConsumerToCollectionIfMissing(consumerCollection, undefined, null);
          expect(expectedResult).toEqual(consumerCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
