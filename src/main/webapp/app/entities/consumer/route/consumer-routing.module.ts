import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ConsumerComponent } from '../list/consumer.component';
import { ConsumerDetailComponent } from '../detail/consumer-detail.component';
import { ConsumerUpdateComponent } from '../update/consumer-update.component';
import { ConsumerRoutingResolveService } from './consumer-routing-resolve.service';

const consumerRoute: Routes = [
  {
    path: '',
    component: ConsumerComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ConsumerDetailComponent,
    resolve: {
      consumer: ConsumerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ConsumerUpdateComponent,
    resolve: {
      consumer: ConsumerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ConsumerUpdateComponent,
    resolve: {
      consumer: ConsumerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(consumerRoute)],
  exports: [RouterModule],
})
export class ConsumerRoutingModule {}
