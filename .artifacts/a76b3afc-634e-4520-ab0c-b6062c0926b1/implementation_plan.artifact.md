# Implementation Plan - Integrate Overpass API in HotelActivity

The user wants to add Overpass API integration to search for hotels in `HotelActivity` and receive a detailed explanation of the implementation.

## User Review Required

> [!IMPORTANT]
> Overpass API queries are based on OpenStreetMap data. The search results will depend on how the location (city name) is tagged in OpenStreetMap. I will implement a query that searches for hotels within a city area.

## Proposed Changes

### Java Source Code

#### [NEW] [OverpassService.java](file:///C:/Users/HariLaam/Downloads/do_an_app_du_lich-master/do_an_app_du_lich-master/app/src/main/java/com/example/doan1/OverpassService.java)
- Define a Retrofit interface for the Overpass API (`https://overpass-api.de/api/`).
- Add a method to fetch data using a `data` query parameter.

#### [NEW] [OverpassResponse.java](file:///C:/Users/HariLaam/Downloads/do_an_app_du_lich-master/do_an_app_du_lich-master/app/src/main/java/com/example/doan1/OverpassResponse.java)
- Create data models to parse the Overpass JSON response.
- Include `Element` and `Tags` classes to capture hotel names and locations.

#### [MODIFY] [HotelActivity.java](file:///C:/Users/HariLaam/Downloads/do_an_app_du_lich-master/do_an_app_du_lich-master/app/src/main/java/com/example/doan1/HotelActivity.java)
- Initialize a new Retrofit instance for Overpass API.
- Implement `performOverpassSearch(String location)` to execute the Overpass QL query.
- Integrate this search into the existing search button logic.
- Display results (e.g., via Toast or logging for now, as the UI doesn't have a dedicated result list yet).

## Verification Plan

### Manual Verification
- Deploy the app.
- Navigate to the Hotel screen.
- Enter a city name (e.g., "Hanoi") and click "Tìm kiếm".
- Check if Overpass API results are fetched and displayed (via Toast/Logcat).

## Detailed Explanation Plan
- I will provide a section-by-section breakdown of:
    - The Overpass QL query structure.
    - How Retrofit handles the request.
    - How the response is parsed using GSON.
    - Why this is beneficial compared to proprietary APIs.
