# FIRS Upgraded Version

This upgraded package focuses on stabilizing the existing project and turning the broken/static dashboards into working data-driven pages.

## What was improved
- Stronger authentication validation
- Password hashing compatibility with automatic migration from legacy plain-text passwords after login
- Better cart quantity handling and stock checks
- Safer order creation flow with validation
- Dynamic product filtering and dealer listing creation
- Working customer dashboard with profile, orders, cart, and checkout
- Working dealer, government, and admin dashboards using backend summaries
- Cleaner environment-based DB credential configuration

## Important notes
- Existing sample accounts from your SQL still work.
- Legacy plain text passwords are upgraded to hashed passwords after successful login.
- Dealer/Government self-registration now creates `PENDING` accounts instead of auto-approval.
- DB credentials are now read from environment variables if present:
  - `DB_USERNAME`
  - `DB_PASSWORD`

## Recommended next upgrades
- Full Spring Security configuration
- Real approval workflow tables
- Audit logs and notification tables
- Dealer-owned inventory model
- License document upload flow
