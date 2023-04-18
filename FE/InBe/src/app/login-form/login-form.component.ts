import { Component, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { FormGroup } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import {  Router } from '@angular/router';
import { GlobalComponent } from '../global-component';
import { LocalstorageService } from '../localstorage.service';
import { UserService } from '../user.service';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css']
})
export class LoginFormComponent implements OnInit {

  constructor(public userService:UserService,public router:Router, public storageService:LocalstorageService,
    private snackBar:MatSnackBar) { }
  loginFormControl=new FormGroup({
    usernameFormControl:new FormControl('',[Validators.required]),
    passwordFormControl:new FormControl('',[Validators.required])
  })
  public get usernameFormControl(){
    return this.loginFormControl.get('usernameFormControl') as FormControl;
  }
  public get passwordFormControl(){
    return this.loginFormControl.get('passwordFormControl') as FormControl;
  }
  ngOnInit(): void {
    if(this.storageService.GetCurrentuesr!=null ||this.storageService.GetCurrentuesr!=undefined){
        // this.isLoading=true;
        this.router.navigate(['/customersList']);
    }
  }
  isLoading:boolean=false;
  Error:any=null;
  passwordType:string="password";
  LoadingContent:string="Signing In"

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
      console.log(this.loginFormControl.value);
      let url=GlobalComponent.apiUrl+"user/login";
      let body={
          email:this.loginFormControl.value['usernameFormControl'],
          password:this.loginFormControl.value['passwordFormControl']
      }
      console.log('body -> ',body);
      this.userService.Signin(url,body).subscribe(
        (r:any)=>{
          this.isLoading=false;
          console.log('r => ',r);
          this.storageService.SetCurrentUser=r.responseBody;
          console.log(this.storageService.GetCurrentuesr);
          this.showSnackBar(r.responseMessage,'Green-Snackbar');
          this.Error="";
          this.router.navigate(['/customersList']);
        },
        e=>{
          this.isLoading=false;
          console.log('error => ',e);
          this.Error=e;
          this.showSnackBar(e.error,'Red-Snackbar');
        }
      )
    }
    else{
      console.log('form is invalid');
      this.isLoading=false;
    }

  }
}
