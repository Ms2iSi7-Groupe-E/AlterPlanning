import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import {AuthModel} from "../models/auth.model";
import {JwtModel} from "../models/jwt.model";
import {Router} from "@angular/router";
import {BehaviorSubject} from "rxjs/BehaviorSubject";
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';

@Injectable()
export class AuthService {

  static readonly LOCAL_STORAGE_KEY_TOKEN = 'JWT_TOKEN';

  public static loggedIn: BehaviorSubject<boolean> = new BehaviorSubject(false);

  constructor(private http: HttpClient, private router: Router) { }

  login(body: AuthModel): Observable<any> {
    return this.http.post('/api/auth', body).map(res =>  {
        if (res && res['access_token']) {
          AuthService.setToken(res['access_token']);
          AuthService.loggedIn.next(true);
        }
        return res;
      });
  }

  logout(logoutInfo?: string) {
    AuthService.loggedIn.next(false);
    AuthService.removeToken();
    this.router.navigate(['/login'], {queryParams: {info: logoutInfo}});
  }

  handleError(error) {
    if (error && error["status"] && error["status"] === 401) {
      this.logout("Votre utilisateur doit se reconnecter");
    }
    return Observable.throw(error);
  }

  static isAuthenticated() {
    const token = localStorage.getItem(AuthService.LOCAL_STORAGE_KEY_TOKEN);
    let isAuthenticated = false;

    if (token) {
      const jwt = new JwtModel(token);

      if (jwt.expireAt() >= new Date()) {
        AuthService.loggedIn.next(true);
        isAuthenticated = true;
      } else {
        AuthService.removeToken();
        AuthService.loggedIn.next(false);
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
