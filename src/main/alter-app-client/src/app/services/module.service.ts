import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import { Observable } from 'rxjs/Observable';

@Injectable()
export class ModuleService {


  constructor(private http: HttpClient) {
  }

  getModules(): Observable<any> {
    return this.http
      .get('/assets/mock/modules.json');
  }
}
