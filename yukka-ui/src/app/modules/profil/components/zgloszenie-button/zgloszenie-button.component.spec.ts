import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ZgloszenieButtonComponent } from './zgloszenie-button.component';

describe('ZgloszenieButtonComponent', () => {
  let component: ZgloszenieButtonComponent;
  let fixture: ComponentFixture<ZgloszenieButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ZgloszenieButtonComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ZgloszenieButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
