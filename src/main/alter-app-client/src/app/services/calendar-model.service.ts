import {Injectable} from '@angular/core';
import {Observable} from "rxjs/Observable";
import {AuthService} from "./auth.service";
import {CalendarModel} from "../models/calendar.model";
import {HttpClient} from "@angular/common/http";
import {CalendarModelModel} from "../models/calendar-model.model";

@Injectable()
export class CalendarModelService {

  constructor(private http: HttpClient, private authService: AuthService) {}

  getModels(): Observable<any> {
    return this.http
      .get('/api/calendar/model', {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }

  addCalendarModel(body: CalendarModelModel): Observable<any> {
    return this.http
      .post('/api/calendar/model', body, {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }

  duplicateCalendarModel(idCalendarModel: number, body: CalendarModelModel): Observable<any> {
    return this.http
      .post('/api/calendar/model/' + idCalendarModel, body, {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }

  deleteModel(idCalendarModel: number): Observable<any> {
    return this.http
      .delete('/api/calendar/model/' + idCalendarModel, {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }
}
