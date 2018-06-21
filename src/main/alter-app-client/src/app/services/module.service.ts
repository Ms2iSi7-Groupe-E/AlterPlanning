import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {AuthService} from './auth.service';
import {RequirementModel} from "../models/requirement.model";

@Injectable()
export class ModuleService {

  constructor(private http: HttpClient, private authService: AuthService) {}

  getModules(): Observable<any> {
    return this.http
      .get('/api/module', {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }

  getRequirementByModule(idModule: string): Observable<any> {
    return this.http
      .get('/api/module/' + idModule + '/requirement', {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }

  addRequirementForModule(idModule: string, body: RequirementModel): Observable<any> {
    return this.http
      .post('/api/module/' + idModule + '/requirement', body,
        {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }

  deleteRequirementByModule(idModule: string, body: RequirementModel): Observable<any> {
    const token = localStorage.getItem(AuthService.LOCAL_STORAGE_KEY_TOKEN);
    const options = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + token
      }),
      body,
    };
    return this.http
      .delete('/api/module/' + idModule + '/requirement', options)
      .catch((err) => this.authService.handleError(err));
  }

  getModulesWithRequirement(): Observable<any> {
    return this.http
      .get('/api/module/with-requirement', {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }
}
