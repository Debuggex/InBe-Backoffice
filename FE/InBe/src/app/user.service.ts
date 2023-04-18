import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LocalstorageService } from './localstorage.service';
import { GlobalComponent } from './global-component';
@Injectable({
  providedIn: 'root'
})
export class UserService {
  Autorization = "";
  constructor(public http: HttpClient, public storageService: LocalstorageService) { }
  CreateUser(url: string, body: any) {
    this.Autorization = this.storageService.GetCurrentuesr.accessToken;
    let header = new HttpHeaders(
      { 'Authorization': 'Bearer ' + this.Autorization }
    );
    let options = {
      headers: header
    }
    return this.http.post(url, body, options);
  }

  CreateSet(url: string, body: any) {
    this.Autorization = this.storageService.GetCurrentuesr.accessToken;
    let header = new HttpHeaders(
      { 'Authorization': 'Bearer ' + this.Autorization }
    );
    let options = {
      headers: header
    }
    return this.http.post(url, body, options);
  }

  UpdateSet(url: string, body: any) {
    this.Autorization = this.storageService.GetCurrentuesr.accessToken;
    let header = new HttpHeaders(
      { 'Authorization': 'Bearer ' + this.Autorization }
    );
    let options = {
      headers: header
    }
    return this.http.put(url, body, options);
  }


  Signin(url: string, body: any) {
    return this.http.post(url, body);
  }

  SignUp(url: string, body: any) {
    this.Autorization = this.storageService.GetCurrentuesr.accessToken;
    let header = new HttpHeaders(
      { 'Authorization': 'Bearer ' + this.Autorization }
    );
    let options = {
      headers: header
    }
    return this.http.post(url, body,options);
  }

  Awake(url: string) {
    this.Autorization = this.storageService.GetCurrentuesr.accessToken;
    let header = new HttpHeaders(
      { 'Authorization': 'Bearer ' + this.Autorization }
    );
    let options = {
      headers: header
    }
    return this.http.get(url, options);
  }

  Awake2(url: string) {
    
    return this.http.get(url);
  }

  getAdminData(url: string) {
    this.Autorization = this.storageService.GetCurrentuesr.accessToken;
    let header = new HttpHeaders(
      { 'Authorization': 'Bearer ' + this.Autorization }
    );
    let options = {
      headers: header
    }
    return this.http.get(url,options);
  }
  

  ChangePassword(url: string, body: {}) {
    this.Autorization = this.storageService.GetCurrentuesr.accessToken;
    let header = new HttpHeaders(
      { 'Authorization': 'Bearer ' + this.Autorization }
    );
    let options = {
      headers: header
    }
    return this.http.put(url, body, options);
  }

  ForgotPassword(url: string, body: {}) {

    return this.http.put(url, body);
  }

  ChangeForgotPassword(url: string, body: {}) {

    return this.http.put(url, body);
  }


  UpdateAccount(url: string, body: {}) {
    this.Autorization = this.storageService.GetCurrentuesr.accessToken;
    let header = new HttpHeaders(
      { 'Authorization': 'Bearer ' + this.Autorization }
    );
    let options = {
      headers: header
    }
    return this.http.put(url, body, options);
  }
  DeleteAccount(url: string, reqbody: any) {
    this.Autorization = this.storageService.GetCurrentuesr.accessToken;
    let header = new HttpHeaders(
      { 'Authorization': 'Bearer ' + this.Autorization }
    );
    let options = {
      headers: header,
      body: reqbody
    }
    return this.http.delete(url, options);
  }

  getData(url: string, body: {}) {
    this.Autorization = this.storageService.GetCurrentuesr.accessToken;
    let header = new HttpHeaders(
      {
        'Authorization': 'Bearer ' + this.Autorization,
        'Content-Type': 'application/json'
      }
    );
    let options = {
      headers: header
    }
    return this.http.post(url, body, options);
  }

  SignOut(url: string) {
    this.Autorization = this.storageService.GetCurrentuesr.accessToken;
    let header = new HttpHeaders(
      { 'Authorization': 'Bearer ' + this.Autorization }
    );
    let options = {
      headers: header
    }
    return this.http.get(url, options);
  }

  getFormResponses(url:string,body:{}){
    this.Autorization = this.storageService.GetCurrentuesr.accessToken;
    let header = new HttpHeaders(
      { 'Authorization': 'Bearer ' + this.Autorization }
    );
    let options = {
      headers: header
    }
    return this.http.post(url,body,options)
  }

  getForm(url:string){
    this.Autorization = this.storageService.GetCurrentuesr.accessToken;
    let header = new HttpHeaders(
      { 'Authorization': 'Bearer ' + this.Autorization }
    );
    let options = {
      headers: header
    }
    return this.http.get(url,options)
  }

  
  
    /**
     * Method is use to download file.
     * @param data - Array Buffer data
     * @param type - type of the document.
     */
     downLoadFile(data: any, type: string,fileName:string) {
      var blob = new Blob([data],{type:type});
      var link = document.createElement('a');
      link.href = window.URL.createObjectURL(blob);
      link.download = fileName;
      link.click();
  }


}
