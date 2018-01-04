import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../services/auth.service";
import {UserService} from "../../services/user.service";
import {CalendarEvent} from 'calendar-utils';
import {Subject} from "rxjs/Subject";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  refresh: Subject<any> = new Subject();
  model;
  calendarViewDate: Date;
  calendarEvents: CalendarEvent[];
  user = {};

  constructor(private authService: AuthService, private userService: UserService) { }

  ngOnInit() {

    this.calendarViewDate = new Date();
    this.calendarEvents = [];
    this.model = {name: "", begin_date: "", end_date: ""};

    this.userService.getMe().subscribe(
      res => {
        this.user = res;
      },
      err => {
        console.error(err);
      }
    );
  }

  addEvent() {
    const color = {
      primary: '#1e90ff',
      secondary: '#D1E8FF'
    };

    const newEvent: CalendarEvent = {
      start: new Date(this.model.begin_date),
      end: new Date(this.model.end_date),
      title: this.model.name,
      color: color,
      allDay: true
    };

    this.model = {name: "", begin_date: "", end_date: ""};

    this.calendarEvents.push(newEvent);
    this.refresh.next(true);
  }

  previousMonth() {
    let newDate = this.calendarViewDate;
    newDate.setMonth(newDate.getMonth() - 1);

    this.calendarViewDate = new Date(newDate);
  }

  nextMonth() {
    let newDate = this.calendarViewDate;
    newDate.setMonth(newDate.getMonth() + 1);

    this.calendarViewDate = new Date(newDate);
  }

  currentMonth() {
    this.calendarViewDate = new Date();
  }

  logout() {
    this.authService.logout();
  }
}
