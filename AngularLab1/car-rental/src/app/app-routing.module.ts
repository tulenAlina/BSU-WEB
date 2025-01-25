import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CarCenterComponent } from './cars/car-center/car-center.component';
import { CarFormComponent } from './car-form/car-form.component';
import { CarListComponent } from './cars/car-list/car-list.component';

const routes: Routes = [
  { path: '', component: CarCenterComponent, children: [
    { path: '', component: CarListComponent }
  ]},
  { path: 'car-form', component: CarFormComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
