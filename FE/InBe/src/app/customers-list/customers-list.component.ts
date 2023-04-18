import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition } from '@angular/material/snack-bar';
import { LocalstorageService, Customer } from './../localstorage.service';
import { UserService } from './../user.service';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { GlobalComponent } from './../global-component';

@Component({
  selector: 'app-customers-list',
  templateUrl: './customers-list.component.html',
  styleUrls: ['./customers-list.component.css']
})
export class CustomersListComponent implements OnInit {


  constructor(public router: Router, public dialog: MatDialog, private snackBar:MatSnackBar,
    private _snackBar: MatSnackBar,
    public storageService: LocalstorageService, public userService: UserService) {
    this.CurrentUser = this.storageService.GetCurrentuesr;
  }
  CurrentUser: any = {};

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  horizontalPosition: MatSnackBarHorizontalPosition = 'center';
  verticalPosition: MatSnackBarVerticalPosition = 'top';

  displayedColumns: string[] = ['id','firstName','lastName','email','Edit', 'View', 'Delete'];
  resultsLength = 0;
  isLoading: boolean = false;
  isView: Boolean = false;
  pageSize: number = 2;
  isEdit: boolean = false;
  public data: MatTableDataSource<Customer>;
  isAdmin:boolean=false;

  

  logOut(){
    this.isLoading=true;
    let url=GlobalComponent.apiUrl+'user/logout';
    this.userService.SignOut(url);
    localStorage.clear();
    this.router.navigate(['/'])
  }

  showSnackBar(){
    this.snackBar.open('Session is expired. Please Re-Login','',{
      panelClass:['Red-Snackbar'],
      duration:3000
    });
  }

  private isTokenExpired() {
    const expiry = (JSON.parse(atob(this.CurrentUser.accessToken.split('.')[1]))).exp;
    console.log(this.CurrentUser.accessToken);
    console.log(Date.now());
    console.log(expiry*1000);
    return expiry * 1000 > Date.now();
  }

  userDetails(data:any){
    this.router.navigate(['customerDetails']);
    this.storageService.SetCustomer=data;
    console.log(this.storageService.GetCustomer);
    
  }

  ngOnInit(): void {
    this.isLoading = true;
    if(this.storageService.GetCurrentuesr==null || this.storageService.GetCurrentuesr==undefined){
        this.isLoading=true;
        this.router.navigate(['/']);      
    }else if (!this.isTokenExpired()) {
      localStorage.clear();
      this.showSnackBar();
      this.router.navigate(['/']);
    } else {

      if (this.storageService.GetCurrentuesr.role=='ADMIN') {
        console.log(this.storageService.GetCurrentuesr.role=='ADMIN');
        this.isAdmin=true;
      }else{
        console.log(this.storageService.GetCurrentuesr.role=='ADMIN');
        this.isAdmin=false;  
      }

      let url = GlobalComponent.apiUrl + "user/getCustomers";
      let body = {
        analystId: this.CurrentUser.id,
        role:this.CurrentUser.role
      }
      this.userService.getData(url, body).subscribe(
        (r: any) => {
          console.log(r);
          this.data = r.responseBody.customerDataResponses;
          this.resultsLength = r.length;
          console.log(r.responseBody.customerDataResponses);
          this.isLoading = false;
          this.data = new MatTableDataSource(r.responseBody.customerDataResponses);
          this.data.paginator = this.paginator;
          this.data.sort = this.sort;
  
        }
      )
    }
    
  }

  
    applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.data.filter = filterValue.trim().toLowerCase();

    if (this.data.paginator) {
      this.data.paginator.firstPage();
    }
  }
}
