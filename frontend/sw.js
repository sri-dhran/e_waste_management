const CACHE_NAME = 'ewaste-cache-v1';
const STATIC_ASSETS = [
    '/index.html',
    '/register.html',
    '/login.html',
    '/dashboard.html',
    '/submitWaste.html',
    '/rewards.html',
    '/center.html',
    '/tracking.html',
    '/admin.html',
    '/reports.html',
    '/css/style.css',
    '/logo-512.png',
    '/manifest.json'
];

// Install Event - Pre-cache assets
self.addEventListener('install', event => {
    event.waitUntil(
        caches.open(CACHE_NAME)
            .then(cache => {
                console.log('PWA Service Worker pre-caching static assets...');
                return cache.addAll(STATIC_ASSETS);
            })
            .then(() => self.skipWaiting())
    );
});

// Activate Event - Clean old caches
self.addEventListener('activate', event => {
    event.waitUntil(
        caches.keys().then(keys => {
            return Promise.all(
                keys.map(key => {
                    if (key !== CACHE_NAME) {
                        console.log('Removing old cache:', key);
                        return caches.delete(key);
                    }
                })
            );
        }).then(() => self.clients.claim())
    );
});

// Fetch Event - Serve from Cache or Network (Bypass API calls)
self.addEventListener('fetch', event => {
    const url = new URL(event.request.url);

    // Bypass caching for backend REST API requests
    if (url.pathname.startsWith('/api/') || event.request.method !== 'GET') {
        event.respondWith(fetch(event.request));
        return;
    }

    event.respondWith(
        caches.match(event.request)
            .then(cachedResponse => {
                if (cachedResponse) {
                    return cachedResponse;
                }
                
                // Fallback to network and dynamically cache new resources if needed
                return fetch(event.request).then(networkResponse => {
                    if (!networkResponse || networkResponse.status !== 200 || networkResponse.type !== 'basic') {
                        return networkResponse;
                    }
                    
                    const responseToCache = networkResponse.clone();
                    caches.open(CACHE_NAME).then(cache => {
                        cache.put(event.request, responseToCache);
                    });
                    
                    return networkResponse;
                });
            }).catch(() => {
                // If offline and request is an HTML page, show cached home
                if (event.request.headers.get('accept').includes('text/html')) {
                    return caches.match('/index.html');
                }
            })
    );
});
