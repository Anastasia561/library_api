from PyPDF2 import PdfReader, PdfWriter
import io
import boto3

def _extract_preview_pages(pdf_bytes: bytes, page_count: int = 5) -> bytes:
    input_stream = io.BytesIO(pdf_bytes)
    reader = PdfReader(input_stream)
    writer = PdfWriter()

    for i in range(min(page_count, len(reader.pages))):
        writer.add_page(reader.pages[i])

    output_stream = io.BytesIO()
    writer.write(output_stream)
    output_stream.seek(0)

    return output_stream.read()

s3 = boto3.client('s3')

def lambda_handler(event, context):
    record = event['Records'][0]
    bucket = record['s3']['bucket']['name']
    input_key = record['s3']['object']['key']
    output_key = input_key.replace("book.pdf", "preview.pdf")

    response = s3.get_object(Bucket=bucket, Key=input_key)
    pdf_bytes = response['Body'].read()

    preview_bytes = _extract_preview_pages(pdf_bytes)

    s3.put_object(
        Bucket=bucket,
        Key=output_key,
        Body=preview_bytes,
        ContentType='application/pdf'
    )

    return {
        'statusCode': 200,
        'message': f"Preview generated and uploaded to {output_key}"
    }