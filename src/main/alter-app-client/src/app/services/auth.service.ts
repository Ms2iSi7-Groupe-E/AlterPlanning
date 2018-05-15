import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import {AuthModel} from "../models/auth.model";
import {JwtModel} from "../models/jwt.model";
import {Router} from "@angular/router";
import 'rxjs/add/operator/map';
import {BehaviorSubject} from "rxjs/BehaviorSubject";

@Injectable()
export class AuthService {

  static readonly LOCAL_STORAGE_KEY_TOKEN = 'JWT_TOKEN';

  public loggedIn: BehaviorSubject<boolean> = new BehaviorSubject(false);

  constructor(private http: HttpClient, private router: Router) { }

  login(body: AuthModel): Observable<any> {
    return this.http.post('/api/auth', body).map(res =>  {
        if (res && res['token']) {
          AuthService.setToken(res['token']);
          this.loggedIn.next(true);
        }
        return res;
      });
  }

  logout() {
    this.loggedIn.next(false);
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
      'Authorization': 'Bearer ' + token
    });
  }

  static setToken(token: string): any {
    localStorage.setItem(AuthService.LOCAL_STORAGE_KEY_TOKEN, token);
  }

  static removeToken() {
    localStorage.removeItem(AuthService.LOCAL_STORAGE_KEY_TOKEN);
  }
}
