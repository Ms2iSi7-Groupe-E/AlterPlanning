import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {AuthService} from './auth.service';

@Injectable()
export class ModuleService {


  constructor(private http: HttpClient) {
  }

  getModules(): Observable<any> {
    return this.http
      .get('/api/module', {headers: AuthService.getHeaders()});
  }
}
