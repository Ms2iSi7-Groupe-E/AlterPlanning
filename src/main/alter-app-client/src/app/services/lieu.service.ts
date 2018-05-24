import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {AuthService} from './auth.service';

@Injectable()
export class LieuService {

  constructor(private http: HttpClient) {
  }

  getLieuxTeachningCourses(): Observable<any> {
    return this.http
      .get('/api/lieu?with-courses=true', {headers: AuthService.getHeaders()});
  }
}
