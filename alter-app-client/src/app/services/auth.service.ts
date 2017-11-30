import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import {AuthModel} from "../models/auth.model";
import {JwtModel} from "../models/jwt.model";
import {Router} from "@angular/router";

@Injectable()
export class AuthService {

  static readonly LOCAL_STORAGE_KEY_TOKEN = 'JWT_TOKEN';

  constructor(private http: HttpClient, private router: Router) { }

  login(body: AuthModel): Observable<any> {
    return this.http
      .post('/api/auth', body);
  }

  logout() {
    AuthService.removeToken();
    this.router.navigate(['/login']);
  }

  static isAuthenticated() {
    const token = localStorage.getItem(AuthService.LOCAL_STORAGE_KEY_TOKEN);
    let isAuthenticated = false;

    if (token) {
      const jwt = new JwtModel(token);

      if (jwt.expireAt() >= new Date()) {
        isAuthenticated = true;
      } else {
        AuthService.removeToken();
      }
    }

    return isAuthenticated;
  }

  static getHeaders(): any {
    const token = localStorage.getItem(AuthService.LOCAL_STORAGE_KEY_TOKEN);

    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': token
    });
  }

  static setToken(token: string): any {
    localStorage.setItem(AuthService.LOCAL_STORAGE_KEY_TOKEN, token);
  }

  static removeToken() {
    localStorage.removeItem(AuthService.LOCAL_STORAGE_KEY_TOKEN);
  }
}
