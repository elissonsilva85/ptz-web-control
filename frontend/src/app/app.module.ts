import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
//import { NgxJoystickModule } from 'ngx-joystick';
import { KeyboardShortcutsModule } from 'ng-keyboard-shortcuts';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatLegacySelectModule as MatSelectModule } from '@angular/material/legacy-select';
import { MatLegacyTabsModule as MatTabsModule } from '@angular/material/legacy-tabs';
import { MatLegacyTooltipModule as MatTooltipModule } from '@angular/material/legacy-tooltip';
import { MatLegacySliderModule as MatSliderModule } from '@angular/material/legacy-slider';
import { MatLegacyFormFieldModule as MatFormFieldModule } from '@angular/material/legacy-form-field';
import { MatLegacyInputModule as MatInputModule } from '@angular/material/legacy-input';
import { MatLegacyDialogModule as MatDialogModule } from '@angular/material/legacy-dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatLegacyCardModule as MatCardModule } from '@angular/material/legacy-card';
import { MatLegacyButtonModule as MatButtonModule } from '@angular/material/legacy-button';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatLegacyMenuModule as MatMenuModule } from '@angular/material/legacy-menu';
import { MatLegacyProgressSpinnerModule as MatProgressSpinnerModule } from '@angular/material/legacy-progress-spinner';
import { MatLegacyAutocompleteModule as MatAutocompleteModule } from '@angular/material/legacy-autocomplete';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { MatLegacyListModule as MatListModule } from '@angular/material/legacy-list';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatLegacySnackBarModule as MatSnackBarModule } from '@angular/material/legacy-snack-bar';

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
import { YoutubeDialog } from './dialog/youtube/youtube.dialog';

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
    YoutubeDialog,
  ],
  imports: [
    FormsModule,
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    // NgxJoystickModule,
    KeyboardShortcutsModule.forRoot(),
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
