import { Component, OnInit } from '@angular/core';
import {DatatableFrench} from "../../helper/datatable-french";

@Component({
  selector: 'app-page-history',
  templateUrl: './page-history.component.html',
  styleUrls: ['./page-history.component.scss']
})
export class PageHistoryComponent implements OnInit {

  dtOptions: DataTables.Settings = {};

  constructor() { }

  ngOnInit() {
    this.dtOptions = {
      order: [],
      columnDefs: [{targets: 'no-sort', orderable: false}],
      language: DatatableFrench.getLanguages(),
    };
  }

}
