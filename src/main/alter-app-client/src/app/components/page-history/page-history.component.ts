import { Component, OnInit } from '@angular/core';
import {DatatableFrench} from "../../helper/datatable-french";
import {HistoryService} from "../../services/history.service";
import {Subject} from "rxjs/Subject";

@Component({
  selector: 'app-page-history',
  templateUrl: './page-history.component.html',
  styleUrls: ['./page-history.component.scss']
})
export class PageHistoryComponent implements OnInit {

  histories = [];
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<void> = new Subject();

  constructor(private historyService: HistoryService) {
    this.dtOptions = {
      order: [],
      columnDefs: [{targets: 'no-sort', orderable: false}],
      language: DatatableFrench.getLanguages(),
    };
  }

  ngOnInit() {
    this.historyService.getAllHistory().subscribe(res => {
      this.histories = res;
      this.dtTrigger.next();
    }, console.error);
  }
}
