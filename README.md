Project CI/CD and GitHub Pages deployment

This repository contains GitHub Actions workflows to build, test and publish artifacts and Javadoc.

Workflows added:

- `.github/workflows/ci.yml` — runs on pushes and PRs to `main`. It runs `mvn test package` and uploads the built JAR as an artifact.
- `.github/workflows/release.yml` — runs when a tag matching `v*` is pushed. It builds the project and creates a GitHub Release attaching the JAR.
- `.github/workflows/gh-pages.yml` — runs on push to `main` (and manually via workflow_dispatch). It generates Javadoc and publishes `target/site/apidocs` to the `gh-pages` branch using `peaceiris/actions-gh-pages`.

How to create a Release (attach built jar):

1. Create a tag locally, e.g.:

```powershell
git tag v1.0.0
git push origin v1.0.0
```

This will trigger the `release.yml` workflow which will build and create a GitHub Release with the JAR attached.

How to publish Javadoc to GitHub Pages:

1. Push to `main` or run the workflow manually from the Actions tab. The generated Javadoc will be available at `https://<your-org-or-user>.github.io/<repo-name>/` once GitHub Pages is enabled for the repository (Pages will serve the `gh-pages` branch by default).

Notes:
- GitHub Pages can only serve static content. A Spring Boot application cannot be hosted on GitHub Pages. The release workflow attaches the executable JAR so you can run it elsewhere (e.g., a VM, container registry, cloud service).
- If you want automatic deployments to a runtime host (Heroku, Render, AWS, GCP, or Azure) instead of Pages, I can add workflows to build a Docker image and push it to GitHub Container Registry or deploy to the provider of your choice.

Security / secrets

Do NOT hardcode database credentials or other secrets in `application.yml` or source files. The workflows in this repository expect secrets to be provided via GitHub Secrets. Recommended approaches:

- Use GitHub repository secrets (Settings → Secrets → Actions) named `DB_URL`, `DB_USER`, `DB_PASSWORD`.
- In CI workflows these are mapped into environment variables and consumed by Spring Boot via placeholders in `application.yml` (see `spring.datasource.*` entries).
- For local development you can set environment variables, use a local `application-local.yml` excluded from git, or use a local `.env` loaded by your IDE (but never commit secrets).

Set secrets via the GitHub web UI or using the `gh` CLI:

```powershell
# using GitHub CLI
gh secret set DB_URL --body "jdbc:postgresql://db-host:5432/dbname"
gh secret set DB_USER --body "dbuser"
gh secret set DB_PASSWORD --body "s3cr3t"
```

More secure options for production:

- Use GitHub Environments with environment-scoped secrets and required reviewers.
- Use a secrets manager (HashiCorp Vault, AWS Secrets Manager, Azure Key Vault) and fetch secrets at runtime with short-lived tokens or OIDC.
- Use Testcontainers for CI tests to avoid sharing real DB credentials (recommended for integration tests).

