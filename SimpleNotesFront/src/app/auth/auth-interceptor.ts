import { HttpInterceptorFn } from '@angular/common/http';
import {inject, PLATFORM_ID} from '@angular/core';
import {isPlatformBrowser} from '@angular/common';

export const authInterceptor: HttpInterceptorFn = (req, next) => {

  const platformId = inject(PLATFORM_ID);


  if (isPlatformBrowser(platformId)) {
    const token = sessionStorage.getItem('token');

    if (!token) {
      return next(req);
    }

    const authReq = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${token}`)
    });

    return next(authReq);
  }


  return next(req);
};
