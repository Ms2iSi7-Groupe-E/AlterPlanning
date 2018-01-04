import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import {AuthService} from "./auth.service";
import {UserModel} from "../models/user.model";
import {ChangePasswordModel} from "../models/change-password.model";

@Injectable()
export class UserService {

  constructor(private http: HttpClient) { }

  getUsers(): Observable<any> {
    return this.http
      .get('/api/users', {headers: AuthService.getHeaders()});
  }

  getUser(uid: String): Observable<any> {
    return this.http
      .get('/api/users/' + uid, {headers: AuthService.getHeaders()});
  }

  getMe(): Observable<any> {
    return this.http
      .get('/api/users/me', {headers: AuthService.getHeaders()});
  }

  addUser(body: UserModel): Observable<any> {
    return this.http
      .post('/api/users', body, {headers: AuthService.getHeaders()});
  }

  updateUser(uid: String, body: UserModel): Observable<any> {
    return this.http
      .put('/api/users/' + uid, body, {headers: AuthService.getHeaders()});
  }

  deleteUser(uid: String): Observable<any> {
    return this.http
      .delete('/api/users/' + uid, {headers: AuthService.getHeaders()});
  }

  changePassword(uid: String, body: ChangePasswordModel): Observable<any> {
    return this.http
      .put('/api/users/' + uid + '/change-password', body, {headers: AuthService.getHeaders()});
  }
}
