jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IConsumer, Consumer } from '../consumer.model';
import { ConsumerService } from '../service/consumer.service';

import { ConsumerRoutingResolveService } from './consumer-routing-resolve.service';

describe('Service Tests', () => {
  describe('Consumer routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ConsumerRoutingResolveService;
    let service: ConsumerService;
    let resultConsumer: IConsumer | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ConsumerRoutingResolveService);
      service = TestBed.inject(ConsumerService);
      resultConsumer = undefined;
    });

    describe('resolve', () => {
      it('should return IConsumer returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultConsumer = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultConsumer).toEqual({ id: 123 });
      });

      it('should return new IConsumer if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultConsumer = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultConsumer).toEqual(new Consumer());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Consumer })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultConsumer = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultConsumer).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
