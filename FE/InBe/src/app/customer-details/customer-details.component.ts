import { Component, OnInit } from '@angular/core';
import { GlobalComponent } from './../global-component';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { LocalstorageService } from './../localstorage.service';
import { UserService } from './../user.service';
import { MatIconRegistry } from '@angular/material/icon';
import { DomSanitizer } from '@angular/platform-browser';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-customer-details',
  templateUrl: './customer-details.component.html',
  styleUrls: ['./customer-details.component.css']
})
export class CustomerDetailsComponent implements OnInit {

  isLoading: boolean = false;
  name:String;
  email:String;
  purpose:String='NA';
  response:any=[];
  instagram:string;
  facebook:string;
  linkedin:string;
  cv:string;
  data:any=[];
  dataObject:any={};

  constructor(public router: Router, public dialog: MatDialog, private snackBar:MatSnackBar,
    private _snackBar: MatSnackBar, private matIconRegistry:MatIconRegistry,private domSanitizer:DomSanitizer,
    public storageService: LocalstorageService, public userService: UserService, private http:HttpClient) {
      
      this.CurrentUser = this.storageService.GetCurrentuesr;
      this.registryIcons();
      this.isLoading = true;
    
  }

  CurrentUser: any = {};

  ngOnInit(): void {
    this.afterInit();
  }

  registryIcons(){
    this.matIconRegistry.addSvgIcon("facebook",this.domSanitizer.bypassSecurityTrustResourceUrl('assets/facebook-svgrepo-com.svg'));
    this.matIconRegistry.addSvgIcon("instagram",this.domSanitizer.bypassSecurityTrustResourceUrl('assets/instagram-svgrepo-com.svg'));
    this.matIconRegistry.addSvgIcon("linkedin",this.domSanitizer.bypassSecurityTrustResourceUrl('assets/linkedin-svgrepo-com.svg'));
    this.matIconRegistry.addSvgIcon("cv",this.domSanitizer.bypassSecurityTrustResourceUrl('assets/document-svgrepo-com.svg'));
    
  }
  downloadFile(file:string){
    console.log(file);
    if(file==undefined || file ==null){
      this.showSnackBar('The user did not upload any CV');
      return;
    }
    this.isLoading=true;
    let url = GlobalComponent.apiUrl+'form/downloadFile';
    let Autorization = this.storageService.GetCurrentuesr.accessToken;
    let header = new HttpHeaders(
      { 'Authorization': 'Bearer ' + Autorization}
    );
    let options = {
      headers: header,
      'responseType'  : 'arraybuffer' as 'json'
    }
    let body = {
      fileURL:file,
      customerId:this.storageService.GetCustomer.id
    }
    let temp = file.split("/");
    let fileName = temp[temp.length-1];
    this.http.post(url,body,options).subscribe((r:any)=>
      {
      const file = new Blob([r], {type: 'application/octet-stream'});
      this.userService.downLoadFile(file,"application/octet-stream",fileName);
      this.isLoading=false;
      });

  }

  downloadCV(){
    
    this.downloadFile(this.cv);
  }

  goToLink(url: string){
    if(url=='facebook'){
      url=this.facebook;
    }
    if(url=='instagram'){
      url=this.instagram;
    }
    if(url=='linkedin'){
      url=this.linkedin;
    }
    window.open(url, "_blank");
  }

  showSnackBar(message:string){
    this.snackBar.open(message,'',{
      panelClass:['Red-Snackbar'],
      duration:3000
    });
  }

  private isTokenExpired() {
    const expiry = (JSON.parse(atob(this.CurrentUser.accessToken.split('.')[1]))).exp;
    // console.log(this.CurrentUser.accessToken);
    // console.log(Date.now());
    // console.log(expiry*1000);
    return expiry * 1000 > Date.now();
  }


  logOut(){
    this.isLoading=true;
    let url=GlobalComponent.apiUrl+'user/logout';
    this.userService.SignOut(url);
    localStorage.clear();
    this.router.navigate(['/'])
  }

  assignLinks(){
    let size = this.response.length;
    for(var i = 0;i<size;i++){
      let temp = this.response[i];
        let str=this.response[i].question;
        if(str.indexOf('Instagram profile')!=-1){
          let url = this.response[i].answer;
          this.instagram=url;
          this.response.splice(i,1);
          i--;
          size--;
          continue;
        }
        if(str.indexOf('Facebook profile')!=-1){
          let url = this.response[i].answer;
          this.facebook=url;
          this.response.splice(i,1);
          i--;
          size--;
          continue;
        }
        if(str.indexOf('LinkedIn profile')!=-1){
          let url = this.response[i].answer;
          this.linkedin=url;
          this.response.splice(i,1);
          i--;
          size--;
          continue;
        }
        if(str.indexOf('Please add your CV')!=-1){
          let url = this.response[i].answer;
          this.cv=url;
          this.response.splice(i,1);
          i--;
          size--;
          continue;
        }      
    }
  }

  preparingCardData(){
    let size = this.response.length;
    for(var i=0;i<this.response.length;i++){

      let question=this.response[i].question;
      let answers=[];

      if(this.response[i].answer==null) {
        this.response.splice(i,1);
        i=i-1;
        size=size-1;
      };
      if(question.indexOf('{{')!=-1){
        let temp = question.split('}}');
        for(var j = 0;j<temp.length;j++){
          if(temp[j][0]=='{'){
            temp[j]=this.storageService.GetCustomer.firstName;
          }
        }
        question='';
        for(var j = 0;j<temp.length;j++){
          question=question+temp[j];
        }
      }

      if(this.response[i].type=='choices'){
        answers=this.response[i].answer.split('\n');
        this.dataObject={
          question:question,
          type:this.response[i].type,
          answer:answers
        }
        this.data[i]=this.dataObject;
        continue;
      }
        answers[0]=this.response[i].answer;
        this.dataObject={
          question:question,
          type:this.response[i].type,
          answer:answers
        }
        this.data[i]=this.dataObject;
        
      
    };
    console.log(this.data);
  }

  excludingNullData(){
    let size = this.response.length;
    for(var i = 0;i<size;i++){
      if(this.response[i].answer==null || this.response[i].answer==undefined){
        console.log(this.response[i]);
        this.response.splice(i,1);
        size=size-1;
        i=i-1;
      }
    }
  }

  getFormResponse(){
    let body={
      customerId:this.storageService.GetCustomer.id
    }
    this.userService.getFormResponses(GlobalComponent.apiUrl+'form/getResponse', body).subscribe(
      (r:any)=>{
        this.response=r;
        console.log(r);
        this.assignLinks();
        this.excludingNullData();
        this.preparingCardData();
      }
    );
    this.name=this.storageService.GetCustomer.firstName+" "+this.storageService.GetCustomer.lastName;
    this.email=this.storageService.GetCustomer.email;
  }

  afterInit(){
    if(this.storageService.GetCurrentuesr==null || this.storageService.GetCurrentuesr==undefined){
        this.isLoading=true;
        this.router.navigate(['/']);      
    }else if (!this.isTokenExpired()) {
      localStorage.clear();
      this.showSnackBar('Session is expired. Please Re-Login');
      this.router.navigate(['/']);
    }else{
      this.getFormResponse();
      this.isLoading=false;
    }
  }

}
