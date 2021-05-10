import { Component, ViewChild } from "@angular/core";
import { ActivatedRoute } from "@angular/router";

import {
  ChartComponent,
  ApexAxisChartSeries,
  ApexChart,
  ApexXAxis,
  ApexDataLabels,
  ApexStroke,
  ApexMarkers,
  ApexYAxis,
  ApexGrid,
  ApexTitleSubtitle,
  ApexLegend
} from "ng-apexcharts";

import { Team } from "src/app/models/team.model";
import { TeamService } from "src/app/services/teams.service";

export type ChartOptions = {
  series: ApexAxisChartSeries;
  chart: ApexChart;
  xaxis: ApexXAxis;
  stroke: ApexStroke;
  dataLabels: ApexDataLabels;
  markers: ApexMarkers;
  colors: string[];
  yaxis: ApexYAxis;
  grid: ApexGrid;
  legend: ApexLegend;
  title: ApexTitleSubtitle;
};

@Component({
  selector: "teamChart",
  templateUrl: "./chart.component.html",
  styleUrls: ["./chart.component.css"]
})
export class TeamChartComponent {
    @ViewChild("chart") chart: ChartComponent;
    public chartOptions: Partial<ChartOptions>;

    id: number;
    team: Team;
    matches: number[] = [];
    recordV: number[] = [];
    recordL: number[] = [];

    constructor(activatedRoute: ActivatedRoute, private teamService: TeamService) {
        this.id = activatedRoute.snapshot.params.id;
        this.chartOptions = {
        series: [
            {
            name: "% Victories",
            data: this.recordV
            },
            {
            name: "% Losses",
            data: this.recordL
            }
        ],
        chart: {
            height: 350,
            type: "line",
            dropShadow: {
            enabled: true,
            color: "#000",
            top: 18,
            left: 7,
            blur: 10,
            opacity: 0.2
            },
            toolbar: {
            show: false
            }
        },
        colors: ["#1AFF00", "#FF0000"],
        dataLabels: {
            enabled: true
        },
        stroke: {
            curve: "smooth"
        },
        title: {
            text: "Team Performance",
            align: "left"
        },
        grid: {
            borderColor: "#e7e7e7",
            row: {
            colors: ["#f3f3f3", "transparent"], // takes an array which will be repeated on columns
            opacity: 0.5
            }
        },
        markers: {
            size: 1
        },
        xaxis: {
            categories: this.matches,
            title: {
            text: "Matches"
            }
        },
        yaxis: {
            title: {
            text: "Percentage"
            },
            min: 0,
            max: 100
        },
        legend: {
            position: "top",
            horizontalAlign: "right",
            floating: true,
            offsetY: -25,
            offsetX: -5
        }
        };
    }

    ngOnInit():void{
        this.teamService.getTeam(this.id).subscribe(
            data => {
              this.team = data;
              this.teamService.getGraph(this.id).subscribe(
                data2 => {
                  this.team.nVictories = data2.nVictories;
                  this.team.nLoses = data2.nLoses;
                  this.team.recordV = data2.recordV;
                  this.team.recordL = data2.recordL;
                  this.getChartValues(this.team);
                }
              )
            },
            error => console.error(error)
          );
    }

    getTeam() {
        return this.team;
      }

    getChartValues(team: Team){
        let nMatches = team.nVictories + team.nLoses;
        for(let i = 1; i <= nMatches; i++){
            this.matches[i-1] = i;
        }
        let victories: number[] = [];
        victories =team.recordV.split(',').map(Number);
        let losses: number[] = [];
        losses = team.recordL.split(',').map(Number); 
        
        for (let i = 0; i < nMatches; i++) {
            this.recordV[i] = Math.round(victories[i]/this.matches[i]*100);
            this.recordL[i] = Math.round(losses[i]/this.matches[i]*100);
        }
    }
}
