import { Component, Input, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { StepByStepAction } from 'src/app/class/step-by-step-action';

@Component({
  selector: 'step-by-step-action',
  templateUrl: './step-by-step-action.component.html',
  styleUrls: ['./step-by-step-action.component.css']
})
export class StepByStepActionComponent implements OnInit {

  @Input() action: StepByStepAction;

  public actionFormGorup: UntypedFormGroup;

  constructor(private fb: UntypedFormBuilder) { }

  ngOnInit(): void {

    //
    let config = this.action
        .getParams()
        .reduce( (acc,cur,idx) => {
          //let disabled = (cur.getType() == 'slider');
          if(cur.getType() != "label")
            acc[`${idx}`] = [ { value: cur.getValue(), disabled: false }, Validators.required ]
          return acc;
        }, {})

    this.actionFormGorup = this.fb.group(config);
    this.actionFormGorup
      .valueChanges
      .subscribe(values => {
        for(var idx in values)
          this.action.setParamValue( parseInt(idx), values[idx] );
      });
  }

}
