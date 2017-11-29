import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/Observable";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  title = 'app';
  test = '';

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    this.getTest().subscribe(
      data => {
        this.test = data.data;
      },
      // Errors will call this callback instead:
      err => {
        console.log(err);
      }
    );
  }

  getTest(): Observable<any> {
    return this.http
      .get('/api/test');
  }

}
