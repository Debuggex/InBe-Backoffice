import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { UserService } from './../user.service';
import { Router } from '@angular/router';
import { LocalstorageService } from './../localstorage.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { GlobalComponent } from './../global-component';

@Component({
  selector: 'app-analyst-signup-form',
  templateUrl: './analyst-signup-form.component.html',
  styleUrls: ['./analyst-signup-form.component.css']
})
export class AnalystSignupFormComponent implements OnInit {

  
  isLoading:boolean=false;
  Error:any=null;
  passwordType:string="password";
  roles:any=['ADMIN','ANALYST'];
  CurrentUser: any = {};

  constructor(public userService:UserService,public router:Router, public storageService:LocalstorageService,
    private snackBar:MatSnackBar) {
      this.CurrentUser = this.storageService.GetCurrentuesr;
     }

    ngOnInit(): void {

      this.isLoading = true;
    if(this.storageService.GetCurrentuesr==null || this.storageService.GetCurrentuesr==undefined){
        this.isLoading=true;
        this.router.navigate(['/']);      
    }else if (!this.isTokenExpired()) {
      localStorage.clear();
      this.showSnackBar('Session is expired. Please Re-Login','Red-Snackbar');
      this.router.navigate(['/']);
    } 
    this.isLoading=false;

    }

  loginFormControl=new FormGroup({
    
    firstNameFormControl:new FormControl('',[Validators.required]),
    lastNameFormControl:new FormControl('',[Validators.required]),
    usernameFormControl:new FormControl('',[Validators.required]),
    passwordFormControl:new FormControl('',[Validators.required]),
    rolesFormControl:new FormControl('',[Validators.required])
  })
  public get usernameFormControl(){
    return this.loginFormControl.get('usernameFormControl') as FormControl;
  }
  public get passwordFormControl(){
    return this.loginFormControl.get('passwordFormControl') as FormControl;
  }
  public get firstNameFormControl(){
    return this.loginFormControl.get('firstNameFormControl') as FormControl;
  }
  public get lastNameFormControl(){
    return this.loginFormControl.get('lastNameFormControl') as FormControl;
  }
  public get rolesFormControl(){
    return this.loginFormControl.get('rolesFormControl') as FormControl;
  }

  private isTokenExpired() {
    const expiry = (JSON.parse(atob(this.CurrentUser.accessToken.split('.')[1]))).exp;
    console.log(this.CurrentUser.accessToken);
    console.log(Date.now());
    console.log(expiry*1000);
    return expiry * 1000 > Date.now();
  }

  
  changeVisible(){

    if (this.passwordType=="password") {
        this.passwordType="text";
    }else{
      this.passwordType="password";
    }
  }


  showSnackBar(message:string,responseType:string){
    this.snackBar.open(message,'',{
      panelClass:[responseType],
      duration:3000
    });
  }


  onSubmit(){
    if(this.loginFormControl.valid){
      this.isLoading=true;
      if (!this.isTokenExpired()) {
        localStorage.clear();
        this.showSnackBar('Session is expired. Please Re-Login','Red-Snackbar');
        this.router.navigate(['/']);
      }else {
      console.log(this.loginFormControl.value);
      let url=GlobalComponent.apiUrl+"user/signup";
      let body={
          firstName:this.loginFormControl.value['firstNameFormControl'],
          lastName:this.loginFormControl.value['lastNameFormControl'],
          role:this.loginFormControl.value['rolesFormControl'],
          email:this.loginFormControl.value['usernameFormControl'],
          password:this.loginFormControl.value['passwordFormControl']
      }
      console.log('body -> ',body);
      this.userService.SignUp(url,body).subscribe(
        (r:any)=>{
          this.isLoading=false;
          this.showSnackBar(r.responseMessage,'Green-Snackbar');
          this.Error="";
          this.router.navigate(['/customersList']);
        },
        e=>{
          this.isLoading=false;
          console.log('error => ',e);
          this.Error=e.error.responseMessage;
          this.showSnackBar(e.error.responseMessage,'Red-Snackbar');
        }
      )
    }
    }
    else{
      console.log('form is invalid');
      this.isLoading=false;
    }

  }
}

  


