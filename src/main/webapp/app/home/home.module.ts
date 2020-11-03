import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ClnTestSharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';

@NgModule({
  imports: [ClnTestSharedModule, RouterModule.forChild([HOME_ROUTE])],
  declarations: [HomeComponent],
})
export class ClnTestHomeModule {}
