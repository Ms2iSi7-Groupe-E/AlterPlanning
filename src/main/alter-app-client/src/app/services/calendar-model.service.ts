import {Injectable} from '@angular/core';
import {Observable} from "rxjs/Observable";
import {AuthService} from "./auth.service";
import {CalendarModel} from "../models/calendar.model";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class CalendarModelService {

  constructor(private http: HttpClient, private authService: AuthService) {}

  getModels(): Observable<any> {
    return this.http
      .get('/api/calendar/models', {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }
}
