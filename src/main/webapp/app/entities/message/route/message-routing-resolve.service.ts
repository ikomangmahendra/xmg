import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMessage, Message } from '../message.model';
import { MessageService } from '../service/message.service';

@Injectable({ providedIn: 'root' })
export class MessageRoutingResolveService implements Resolve<IMessage> {
  constructor(protected service: MessageService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMessage> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((message: HttpResponse<Message>) => {
          if (message.body) {
            return of(message.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Message());
  }
}
