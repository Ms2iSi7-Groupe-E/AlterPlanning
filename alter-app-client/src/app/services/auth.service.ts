import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import {AuthModel} from "../models/auth.model";

@Injectable()
export class AuthService {

  constructor(private http: HttpClient) { }

  auth(user: AuthModel): Observable<any> {
    return this.http
      .post('/api/auth', user);
  }

  public static getHeaders(): any {
    const token = localStorage.getItem("token");

    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': token
    });
  }

  public static setToken(token: string): any {
    localStorage.setItem("token", token);
  }
}
