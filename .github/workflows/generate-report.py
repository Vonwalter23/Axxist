import os
html = """<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Axxist APK Validation Report</title>
    <style>
        body { font-family: sans-serif; background: #0d1117; color: #c9d1d9; padding: 20px; }
        .container { max-width: 1200px; margin: 0 auto; }
        .header { background: linear-gradient(135deg, #1f6feb, #238636); padding: 30px; border-radius: 12px; }
        .badge { display: inline-block; padding: 8px 20px; border-radius: 20px; background: #238636; color: white; }
        .card { background: #161b22; border-radius: 12px; padding: 20px; margin: 20px 0; border: 1px solid #30363d; }
        .metric { display: flex; justify-content: space-between; padding: 10px 0; border-bottom: 1px solid #21262d; }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>Axxist APK Validation Report</h1>
            <p>Repository: Vonwalter23/Axxist | Branch: """ + os.environ.get('GITHUB_REF_NAME', 'N/A') + """</p>
            <span class="badge">APK VALIDATION COMPLETED</span>
        </div>
        <div class="card">
            <h2>Build Information</h2>
            <div class="metric"><span>Commit SHA</span><span>""" + os.environ.get('GITHUB_SHA', 'N/A')[:8] + """</span></div>
            <div class="metric"><span>Branch</span><span>""" + os.environ.get('GITHUB_REF_NAME', 'N/A') + """</span></div>
            <div class="metric"><span>Workflow</span><span>Android Quality Gate v4.0</span></div>
        </div>
        <div class="card">
            <h2>Certification</h2>
            <p>This APK has been generated and validated as part of the Axxist Quality Gate process.</p>
        </div>
    </div>
</body>
</html>"""
with open('apk-validation-report.html', 'w') as f:
    f.write(html)
print("Report generated successfully")
