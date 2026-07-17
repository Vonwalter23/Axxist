# APK Validation Policy

## Purpose

This document establishes the validation requirements for APK generation in the Axxist project.

**Core Principle**: No Stage shall be considered fully validated solely because the code compiles. When applicable, there must exist an installable and executable APK that represents the actual state of the project.

---

## Validation Philosophy

### Why APK Validation Matters

1. **Code Compilation ≠ Functionality**: A project that compiles successfully may still have runtime issues
2. **Reproducibility**: APK artifacts prove that the build process is reproducible
3. **Delivery Readiness**: APK files are the actual deliverable for Android applications
4. **Quality Assurance**: APK validation catches issues that lint or compilation might miss

### When APK Validation is Required

| Scenario | APK Required | Reason |
|----------|--------------|--------|
| Stage Completion | Yes | Demonstrates functional state |
| Pull Request Merge | Yes | Ensures no regression in build |
| Pre-Release | Yes + Runtime | Full validation before distribution |
| Hotfix | Yes | Quick turnaround validation |
| Documentation Update | No | No functional change |

---

## APK Validation Checklist

Every APK validation must verify:

### Build Quality Gate (Automated)

- [ ] **APK Generated**: Debug APK (`Axxist-debug.apk`) created
- [ ] **APK Generated**: Release APK (`Axxist-release.apk`) created
- [ ] **APK Valid**: File size is reasonable (not 0 bytes, not corrupted)
- [ ] **Lint Passed**: No critical lint errors blocking release
- [ ] **SHA256 Generated**: Hash computed for integrity verification
- [ ] **Artifact Published**: APK available in GitHub Actions artifacts

### Installation Validation (Optional/Release)

- [ ] **APK Installable**: Successfully installed via `adb install -r`
- [ ] **Package Registered**: Appears in `pm list packages`
- [ ] **No Signature Issues**: Installation completed without warnings

### Execution Validation (Optional/Release)

- [ ] **App Launches**: Application starts without immediate crash
- [ ] **No Fatal Exceptions**: Logcat shows no `FATAL EXCEPTION`
- [ ] **No ANR**: No "Application Not Responding" events
- [ ] **Runtime Stable**: Application remains stable for minimum duration

---

## Artifact Policy

### Required Artifacts

Every successful build must produce:

| Artifact | Format | Retention | Purpose |
|----------|--------|-----------|---------|
| Axxist-debug.apk | Binary | 30 days | Testing distribution |
| Axxist-release.apk | Binary | 30 days | Production distribution |
| apk-validation-report.html | HTML | 30 days | Build certification |
| build-summary.txt | Text | 30 days | Quick reference |

### Runtime Validation Artifacts (On-Demand)

| Artifact | Format | Retention | Purpose |
|----------|--------|-----------|---------|
| runtime-logcat.txt | Text | 30 days | Crash investigation |
| crash-analysis.txt | Text | 30 days | Crash summary |
| emulator-info.txt | Text | 30 days | Environment details |
| runtime-report.html | HTML | 30 days | Runtime certification |

### Artifact Naming Convention

All APKs must follow this naming:

- Debug: `Axxist-debug-{YYYY-MM-DD}-{commit-sha7}.apk`
- Release: `Axxist-release-{version}-{YYYY-MM-DD}.apk`

Note: GitHub Actions artifacts use simplified names due to platform limitations.

---

## APK Metadata Requirements

Each APK must have associated metadata:

```yaml
apk_metadata:
  filename: Axxist-debug.apk
  size: "29.4 MB"
  sha256: "a1b2c3d4e5f6..."
  build_type: "debug"
  commit: "4d499b3"
  branch: "main"
  build_date: "2024-07-17"
  package_name: "com.axxist.app"
  version_name: "0.0.9"
  version_code: 9
  min_sdk: 29
  target_sdk: 34
```

---

## APK Download and Verification

### Download APKs from GitHub Actions

1. Navigate to the workflow run in GitHub Actions
2. Go to the "Artifacts" section
3. Download `Axxist-debug.apk` or `Axxist-release.apk`
4. Verify SHA256 hash matches the reported value

### Verify APK Integrity

```bash
# Download APK
curl -L -o Axxist-debug.apk "https://github.com/Vonwalter23/Axxist/releases/download/v0.0.9/Axxist-debug.apk"

# Verify hash
sha256sum Axxist-debug.apk
```

---

## APK Validation Workflow

### Build Quality Gate (Automatic)

Triggered by: `push` to `main` or `develop`, or `pull_request`

```
┌─────────────────┐
│  Build Debug    │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ Build Release   │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│   Run Lint      │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ Generate Report │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  Final Status   │
└─────────────────┘
```

### Runtime Validation (On-Demand)

Triggered by: `workflow_dispatch`

```
┌─────────────────┐
│Environment Setup│
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  Emulator Boot  │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  APK Install    │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  App Launch     │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│Runtime Monitor  │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  Crash Analysis │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  Final Status   │
└─────────────────┘
```

---

## Version History

| Version | Date | Changes |
|---------|------|---------|
| 1.0 | 2024-07-17 | Initial policy creation |

---

## Related Documents

- [Release Validation Policy](RELEASE_VALIDATION_POLICY.md)
- [Development Policy](DEVELOPMENT_POLICY.md)
- [Branch Protection Setup](GITHUB_BRANCH_PROTECTION.md)

---

## Policy Owner

**Axxist Quality Team**

For questions or clarifications, refer to the project maintainers.

---

**Last Updated**: 2024-07-17  
**Policy Version**: 1.0
