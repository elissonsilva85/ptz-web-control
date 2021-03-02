import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgxJoystickModule } from 'ngx-joystick';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatSelectModule } from '@angular/material/select';
import { MatTabsModule } from '@angular/material/tabs';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSliderModule } from '@angular/material/slider';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatMenuModule } from '@angular/material/menu';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { MatListModule } from '@angular/material/list';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatSnackBarModule } from '@angular/material/snack-bar';

import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { MainComponent } from './main/main.component';
import { NumpadComponent } from './component/numpad/numpad.component';
import { ZoomComponent } from './component/zoom/zoom.component';
import { FocusComponent } from './component/focus/focus.component';
import { JoystickComponent } from './component/joystick/joystick.component';
import { IrisComponent } from './component/iris/iris.component';

import { RegionDialog } from './dialog/region/region.dialog';
import { PresetNamesDialog } from './dialog/preset-names/preset-names.dialog';
import { BrightnessContrastDialog } from './dialog/brightness-contrast/brightness-contrast.dialog';
import { StepByStepDialog } from './dialog/step-by-step/step-by-step.dialog';
import { LogDialog } from './dialog/log/log.dialog';
import { StepByStepActionComponent } from './component/step-by-step-action/step-by-step-action.component';
import { MilisecondFormatPipe } from './component/milisecond-format/milisecond-format.pipe';
import { ConfirmDialog } from './dialog/confirm/confirm.dialog';

@NgModule({
  declarations: [
    AppComponent,
    MainComponent,
    NumpadComponent,
    ZoomComponent,
    FocusComponent,
    JoystickComponent,
    IrisComponent,
    //
    RegionDialog,
    PresetNamesDialog,
    BrightnessContrastDialog,
    StepByStepDialog,
    LogDialog,
    StepByStepActionComponent,
    MilisecondFormatPipe,
    ConfirmDialog,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    NgxJoystickModule,
    //    
    BrowserAnimationsModule,
    //
    MatSelectModule,
    MatTabsModule,
    MatTooltipModule,
    MatSliderModule,
    MatFormFieldModule,
    MatInputModule,
    MatMenuModule,
    MatDialogModule,
    MatIconModule,
    MatCardModule,
    MatButtonModule,
    MatToolbarModule,
    MatProgressSpinnerModule,
    MatAutocompleteModule,
    DragDropModule,
    MatListModule,
    MatExpansionModule,
    MatButtonToggleModule,
    MatSnackBarModule,
    //
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
