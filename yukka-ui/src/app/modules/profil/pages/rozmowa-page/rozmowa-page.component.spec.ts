import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RozmowaPageComponent } from './rozmowa-page.component';

describe('RozmowaPageComponent', () => {
  let component: RozmowaPageComponent;
  let fixture: ComponentFixture<RozmowaPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RozmowaPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RozmowaPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
