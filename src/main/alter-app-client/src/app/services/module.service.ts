import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {AuthService} from './auth.service';
import {RequirementModel} from "../models/requirement.model";

@Injectable()
export class ModuleService {


  constructor(private http: HttpClient) {
  }

  getModules(): Observable<any> {
    return this.http
      .get('/api/module', {headers: AuthService.getHeaders()});
  }

  addRequirementForModule(idModule: string, body: RequirementModel): Observable<any> {
    return this.http
      .post('/api/module/' + idModule + '/requirement', body,
        {headers: AuthService.getHeaders()} );
  }

  getModulesWithRequirement(): Observable<any> {
    return this.http
      .get('/api/module/with-requirement', {headers: AuthService.getHeaders()});
  }
}
