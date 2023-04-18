import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HashLocationStrategy, LocationStrategy } from '@angular/common';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule } from '@angular/router';
import { LoginFormComponent } from './login-form/login-form.component';
import {MatGridListModule} from '@angular/material/grid-list';
import { UserService } from './user.service';
import { LocalstorageService } from './localstorage.service';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field'
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatNativeDateModule } from '@angular/material/core';
import { MatMenuModule } from '@angular/material/menu';
import { MatIconModule } from '@angular/material/icon';
import { MatDialogModule } from '@angular/material/dialog';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatStepper, MatStepperModule, MatVerticalStepper } from '@angular/material/stepper';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatSortModule } from '@angular/material/sort';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatSelectModule } from '@angular/material/select';
import { HttpClient,HttpClientModule } from '@angular/common/http';
import { CustomersListComponent } from './customers-list/customers-list.component';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { CustomerDetailsComponent } from './customer-details/customer-details.component';
import { AnalystSignupFormComponent } from './analyst-signup-form/analyst-signup-form.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginFormComponent,
    CustomersListComponent,
    CustomerDetailsComponent,
    AnalystSignupFormComponent
  ],
  imports: [
  
  BrowserModule,
    BrowserAnimationsModule,
    MatGridListModule,
    BrowserModule,
    BrowserAnimationsModule,
    MatNativeDateModule,
    FormsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatButtonModule,
    MatInputModule,
    MatMenuModule,
    MatIconModule,
    MatDialogModule,
    MatProgressSpinnerModule,
    MatStepperModule,
    MatFormFieldModule,
    MatTableModule,
    MatSortModule,
    MatCheckboxModule,
    MatCardModule,
    MatDividerModule,
    MatSelectModule,
    HttpClientModule,
    MatSnackBarModule,
    MatPaginatorModule,
    MatSortModule,
    MatIconModule,
    MatButtonModule,
    MatGridListModule,
    MatCardModule,
    RouterModule.forRoot([
      {path:'',component:LoginFormComponent},
      {path:'customersList',component:CustomersListComponent},
      {path:'customerDetails',component:CustomerDetailsComponent},
      {path:'analystSignUp',component:AnalystSignupFormComponent}
    ])
  ],
  providers: [UserService, LocalstorageService,HttpClient, { provide: LocationStrategy, useClass: HashLocationStrategy }],
  bootstrap: [AppComponent]
})
export class AppModule { }
