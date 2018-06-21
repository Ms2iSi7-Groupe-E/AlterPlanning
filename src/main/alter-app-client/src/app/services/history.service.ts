import {Injectable} from '@angular/core';
import {Observable} from "rxjs/Observable";
import {AuthService} from "./auth.service";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class HistoryService {

  constructor(private http: HttpClient, private authService: AuthService) {}

  getAllHistory(): Observable<any> {
    return this.http
      .get('/api/history', {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }
}
