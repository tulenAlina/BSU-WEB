import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CarCenterComponent } from './car-center/car-center.component';
import { CarListComponent } from './car-list/car-list.component';
import { CarDetailsComponent } from './car-details/car-details.component';

const routes: Routes = [
  { path: '', component: CarCenterComponent, children: [
    { path: 'details/:id', component: CarDetailsComponent }
  ]}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CarsRoutingModule { }