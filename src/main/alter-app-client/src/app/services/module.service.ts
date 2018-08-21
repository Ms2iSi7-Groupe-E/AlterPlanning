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

  getModulesWithRequirement(): Observable<any> {
    return this.http
      .get('/api/module/requirement', {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }

  deleteRequirementById(idModuleRequirement: number): Observable<any> {
    return this.http
      .delete('/api/module/requirement/' + idModuleRequirement, {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }
}
