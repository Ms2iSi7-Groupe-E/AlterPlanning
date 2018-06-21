import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import {AuthService} from "./auth.service";
import {UserModel} from "../models/user.model";
import {ChangePasswordModel} from "../models/change-password.model";

@Injectable()
export class UserService {

  constructor(private http: HttpClient, private authService: AuthService) {}

  getUsers(): Observable<any> {
    return this.http
      .get('/api/users', {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }

  getUser(id: number): Observable<any> {
    return this.http
      .get('/api/users/' + id, {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }

  getMe(): Observable<any> {
    return this.http
      .get('/api/users/me', {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }

  addUser(body: UserModel): Observable<any> {
    return this.http
      .post('/api/users', body, {headers: AuthService.getHeaders()});
  }

  updateUser(id: number, body: UserModel): Observable<any> {
    return this.http
      .put('/api/users/' + id, body, {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }

  deleteUser(id: number): Observable<any> {
    return this.http
      .delete('/api/users/' + id, {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }

  changePassword(id: number, body: ChangePasswordModel): Observable<any> {
    return this.http
      .put('/api/users/' + id + '/change-password', body, {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }
}
