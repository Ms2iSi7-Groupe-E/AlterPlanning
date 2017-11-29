import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import {AuthService} from "./auth.service";

@Injectable()
export class UserService {

  constructor(private http: HttpClient) { }

  getUsers(): Observable<any> {
    return this.http
      .get('/api/users', {headers: AuthService.getHeaders()});
  }

  getUser(uid: String): Observable<any> {
    return this.http
      .get('/api/user/' + uid, {headers: AuthService.getHeaders()});
  }

  getMe(): Observable<any> {
    return this.http
      .get('/api/user', {headers: AuthService.getHeaders()});
  }

  addUser(body): Observable<any> {
    return this.http
      .post('/api/user', body, {headers: AuthService.getHeaders()});
  }

  updateUser(uid: String, body): Observable<any> {
    return this.http
      .put('/api/user/' + uid, body, {headers: AuthService.getHeaders()});
  }

  deleteUser(uid: String): Observable<any> {
    return this.http
      .delete('/api/user/' + uid, {headers: AuthService.getHeaders()});
  }

  changePassword(uid: String, body): Observable<any> {
    return this.http
      .put('/api/user/change-password/' + uid, body, {headers: AuthService.getHeaders()});
  }
}
