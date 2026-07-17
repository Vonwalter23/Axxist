# Release Validation Policy

## Purpose

This document establishes the requirements for releasing the Axxist project. All releases must pass rigorous validation to ensure quality and stability.

---

## Pre-Release Requirements

Before any release, the following validations are **MANDATORY**:

### Build Quality Gate (Required)

| Check | Status | Description |
|-------|--------|-------------|
| Build Quality Gate | ✅ PASS | All builds must pass |
| Debug APK Generated | ✅ PASS | APK created successfully |
| Release APK Generated | ✅ PASS | Release APK created |
| Lint Analysis | ✅ PASS | No critical lint errors |

### Runtime Validation (Required for Major Releases)

| Check | Status | Description |
|-------|--------|-------------|
| Emulator Boot | ✅ PASS | Android 14 API 34 booted |
| APK Installation | ✅ PASS | Successfully installed |
| Application Launch | ✅ PASS | App starts without crash |
| Runtime Duration | ✅ PASS | Stable for minimum 180 seconds |
| Crash Detection | ✅ PASS | No crashes detected |

### APK Certification (Required)

| Check | Status | Description |
|-------|--------|-------------|
| APK Downloadable | ✅ PASS | Available in artifacts |
| SHA256 Generated | ✅ PASS | Hash computed |
| Size Valid | ✅ PASS | Not corrupted (not 0 bytes) |
| Installable | ✅ PASS | Can be installed on device |

---

## Release Checklist

### Pre-Release Phase

```
□ Update CHANGELOG.md with release notes
□ Update version in build.gradle
□ Verify all tests pass locally
□ Run Android Quality Gate
□ Verify all APKs generated
□ Download and verify SHA256
□ Run Android Runtime Validation (major releases)
□ Review crash analysis report
□ Verify all artifacts published
```

### Release Decision

```
□ All Build Quality Gate checks PASS
□ All Runtime Validation checks PASS (if applicable)
□ All APK Certification checks PASS
□ Documentation updated
□ CHANGELOG.md updated
□ Version number bumped
□ Release artifacts available
```

### Post-Release

```
□ Tag created with version
□ Release notes published
□ Artifacts archived
□ Version bump committed
□ Notification sent (if applicable)
```

---

## Release Types

### Patch Release (x.x.Z)

**Trigger**: Bug fixes, small improvements

**Requirements**:
- Build Quality Gate: PASS
- Debug APK: Generated
- Lint: No new warnings

**Approval**: Automated (CI/CD)

### Minor Release (x.Y.0)

**Trigger**: New features, backward compatible

**Requirements**:
- Build Quality Gate: PASS
- Debug APK: Generated
- Release APK: Generated
- Lint: PASS

**Approval**: Automated (CI/CD)

### Major Release (Y.0.0)

**Trigger**: Breaking changes, significant milestones

**Requirements**:
- Build Quality Gate: PASS
- Debug APK: Generated
- Release APK: Generated
- Lint: PASS
- Runtime Validation: PASS

**Approval**: Manual review required

---

## Validation Workflow

### Automated Validation (CI/CD)

Triggered automatically on every push to protected branches:

```
┌─────────────────────────────────────────┐
│         Android Quality Gate             │
├─────────────────────────────────────────┤
│  Build Validation                       │
│  ├── Debug APK ✓                       │
│  ├── Release APK ✓                     │
│  └── Lint Analysis ✓                  │
│                                          │
│  APK Validation Report ✓               │
│  ├── SHA256 Generated ✓               │
│  ├── Size Recorded ✓                  │
│  └── Artifacts Published ✓             │
│                                          │
│  Final Status: PASS/FAIL ✓             │
└─────────────────────────────────────────┘
```

### Manual Validation (Releases)

Triggered via workflow_dispatch:

```
┌─────────────────────────────────────────┐
│       Android Runtime Validation         │
├─────────────────────────────────────────┤
│  Environment Setup                      │
│  ├── APK Available ✓                   │
│  └── Package Ready ✓                   │
│                                          │
│  Emulator Setup                         │
│  ├── API 34 ✓                          │
│  └── Boot Complete ✓                    │
│                                          │
│  Installation                           │
│  ├── adb install ✓                     │
│  └── Package Registered ✓              │
│                                          │
│  Launch                                 │
│  ├── MainActivity ✓                    │
│  └── Process Running ✓                 │
│                                          │
│  Runtime Monitoring                      │
│  ├── Duration: 180s ✓                  │
│  └── Logcat Captured ✓                 │
│                                          │
│  Crash Analysis                         │
│  ├── FATAL EXCEPTION: 0 ✓              │
│  ├── AndroidRuntime: 0 ✓               │
│  └── ANR: 0 ✓                         │
│                                          │
│  Final Status: PASS/FAIL ✓             │
└─────────────────────────────────────────┘
```

---

## Release Validation Matrix

| Release Type | Quality Gate | Runtime Validation | Manual Review |
|--------------|--------------|-------------------|---------------|
| Patch (x.x.Z) | Required | Optional | Not Required |
| Minor (x.Y.0) | Required | Optional | Not Required |
| Major (Y.0.0) | Required | Required | Required |

---

## APK Delivery Requirements

### Artifact Naming

```
Axxist-{version}-{build-type}-{date}.apk
```

Examples:
- `Axxist-0.0.9-debug-20240717.apk`
- `Axxist-0.0.9-release-20240717.apk`

### Artifact Metadata

Each release must include:

```yaml
release_metadata:
  version: "0.0.9"
  version_code: 9
  build_type: "release"
  commit: "4d499b3"
  build_date: "2024-07-17"
  artifacts:
    debug_apk:
      name: "Axxist-debug.apk"
      size: "29.4 MB"
      sha256: "a1b2c3d4..."
    release_apk:
      name: "Axxist-release.apk"
      size: "14.3 MB"
      sha256: "e5f6a7b8..."
  validation:
    quality_gate: "PASS"
    runtime_validation: "PASS"
    crash_count: 0
```

---

## Release Blocking Criteria

A release **MUST NOT** proceed if:

1. **Build Failure**: Any gradle build task fails
2. **Critical Lint Errors**: Any lint error marked as fatal
3. **APK Not Generated**: APK file does not exist or is corrupted
4. **Runtime Crash**: Any `FATAL EXCEPTION` detected
5. **ANR Detected**: Any "Application Not Responding" event
6. **Installation Failure**: APK cannot be installed on emulator

---

## Version Numbering

Axxist follows [Semantic Versioning](https://semver.org/):

```
MAJOR.MINOR.PATCH
     │     │     └── Patch: Bug fixes
     │     └────────── Minor: New features (backward compatible)
     └───────────────── Major: Breaking changes
```

Current Version: `0.0.9` (Pre-release)

---

## Version History

| Version | Date | Changes |
|---------|------|---------|
| 1.0 | 2024-07-17 | Initial policy creation |

---

## Related Documents

- [APK Validation Policy](APK_VALIDATION_POLICY.md)
- [Development Policy](DEVELOPMENT_POLICY.md)
- [Branch Protection Setup](GITHUB_BRANCH_PROTECTION.md)
- [CHANGELOG](../CHANGELOG.md)

---

## Policy Owner

**Axxist Release Team**

For questions or clarifications, refer to the project maintainers.

---

**Last Updated**: 2024-07-17  
**Policy Version**: 1.0
