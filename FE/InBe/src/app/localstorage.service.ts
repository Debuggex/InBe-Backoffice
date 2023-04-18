import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LocalstorageService {

  constructor() {
  }

  // Users

  public set SetCurrentUser(value: any) {
    if (value == null) {
      localStorage.removeItem('User');
    }
    else {
      localStorage.setItem('User', JSON.stringify(value))
    }
  }

  public get GetCurrentuesr() {
    let a = localStorage.getItem('User') as string;
    return JSON.parse(a) as User;
  }

  // Customers

  public set SetCustomer(value: any) {
    if (value == null) {
      localStorage.removeItem('Customer');
    }
    else {
      localStorage.setItem('Customer', JSON.stringify(value))
    }
  }

  public get GetCustomer() {
    let a = localStorage.getItem('Customer') as string;
    return JSON.parse(a) as Customer;
  }

  Remove(key: string) {

  }

  
}
export interface User {
  "id": number,
  "email": string,
  "firstName": string,
  "lastName": string,
  "accessToken": string,
  "refreshToken": string,
  "role": string
}

export interface Customer{
  "id":number,
  "email":string,
  "firstName":string,
  "lastName":string
}