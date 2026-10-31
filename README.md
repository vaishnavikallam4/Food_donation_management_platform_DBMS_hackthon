# FoodBridge — Three-role Food Donation Management System

FoodBridge has exactly three account roles: **Donor, NGO, and Volunteer**. It has no Admin account.

## Correct data visibility
- Donors see only donations they personally published and requests made against those donations.
- NGOs see all currently available donations from all donors and only their own food requests.
- Volunteers see only delivery assignments assigned to their account.
- The Food Requests badge and dashboard pending-request counts are scoped to the signed-in account, not global pending requests.
- The frontend refreshes data after actions, when the browser tab regains focus, and every 10 seconds while signed in.

## Workflow
1. Donor registers and publishes a donation.
2. NGO browses available donations and requests a quantity.
3. The donor who owns that donation sees the request and accepts or rejects it.
4. The donor assigns a registered volunteer to an approved request.
5. The assigned volunteer accepts, marks pickup, and marks delivery complete.
6. The NGO and donor can track the request and assignment status.

## Requirements
- Java 17+
- Maven 3.9+
- Node.js 18+
- MySQL 8+

## Database
Create the database if needed:

```sql
CREATE DATABASE food_donation_db;
```

Set your local MySQL username/password in `backend/src/main/resources/application.properties`. Do not share or commit real credentials. Existing database records are retained with Hibernate `ddl-auto=update`; back up the database before changing project versions.

## Run backend
```powershell
cd backend
mvn spring-boot:run
```
Backend URL: `http://localhost:8080`

## Run frontend in a second terminal
```powershell
cd frontend
npm install
npm run dev
```
Open the Vite URL shown in the terminal, usually `http://localhost:5173`.

## Important
A donor's empty Food Donations page is expected until that donor publishes a donation. Donors must not see another donor's private management list. NGOs are the role that browses all available donations. A donor's request badge must show only pending requests for that donor's own donations.

The source was updated to correct account-scoped dashboard counts and refresh behavior. It has not been fully end-to-end tested against your Windows/MySQL setup; run it locally and verify with one account for each role.
