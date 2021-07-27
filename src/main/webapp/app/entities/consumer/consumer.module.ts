import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ConsumerComponent } from './list/consumer.component';
import { ConsumerDetailComponent } from './detail/consumer-detail.component';
import { ConsumerUpdateComponent } from './update/consumer-update.component';
import { ConsumerDeleteDialogComponent } from './delete/consumer-delete-dialog.component';
import { ConsumerRoutingModule } from './route/consumer-routing.module';

@NgModule({
  imports: [SharedModule, ConsumerRoutingModule],
  declarations: [ConsumerComponent, ConsumerDetailComponent, ConsumerUpdateComponent, ConsumerDeleteDialogComponent],
  entryComponents: [ConsumerDeleteDialogComponent],
})
export class ConsumerModule {}
