import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CarsRoutingModule } from './cars-routing.module';
import { CarCenterComponent } from './car-center/car-center.component';
import { CarListComponent } from './car-list/car-list.component';
import { CarDetailsComponent } from './car-details/car-details.component';
import { CarFormComponent } from '../car-form/car-form.component';
import { FormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    CarCenterComponent,
    CarListComponent,
    CarDetailsComponent,
    CarFormComponent
  ],
  imports: [
    CommonModule,
    CarsRoutingModule,
    FormsModule
  ]
})
export class CarsModule { }