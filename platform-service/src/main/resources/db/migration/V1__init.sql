create extension if not exists vector;

create table tenants (
    id uuid primary key,
    created_at timestamptz not null,
    updated_at timestamptz not null,
    name varchar(255) not null,
    slug varchar(120) not null unique,
    status varchar(40) not null
);

create table user_accounts (
    id uuid primary key,
    created_at timestamptz not null,
    updated_at timestamptz not null,
    email varchar(255) not null unique,
    password_hash varchar(255) not null,
    full_name varchar(255) not null,
    tenant_id uuid not null references tenants(id),
    enabled boolean not null
);

create table user_roles (
    user_id uuid not null references user_accounts(id) on delete cascade,
    role varchar(60) not null,
    primary key (user_id, role)
);

create table ai_assistants (
    id uuid primary key,
    created_at timestamptz not null,
    updated_at timestamptz not null,
    tenant_id uuid not null references tenants(id),
    name varchar(255) not null,
    slug varchar(160) not null,
    system_prompt varchar(4000) not null,
    model_name varchar(120) not null,
    status varchar(40) not null
);

create index idx_ai_assistants_tenant on ai_assistants(tenant_id);

create table knowledge_documents (
    id uuid primary key,
    created_at timestamptz not null,
    updated_at timestamptz not null,
    tenant_id uuid not null references tenants(id),
    assistant_id uuid not null references ai_assistants(id),
    title varchar(255) not null,
    source_type varchar(40) not null,
    content_hash varchar(64) not null,
    status varchar(40) not null
);

create index idx_knowledge_documents_assistant on knowledge_documents(tenant_id, assistant_id);

create table document_chunks (
    id uuid primary key,
    created_at timestamptz not null,
    updated_at timestamptz not null,
    tenant_id uuid not null references tenants(id),
    assistant_id uuid not null references ai_assistants(id),
    document_id uuid not null references knowledge_documents(id) on delete cascade,
    chunk_index integer not null,
    content text not null,
    token_estimate integer not null,
    embedding vector(64) not null
);

create index idx_document_chunks_lookup on document_chunks(tenant_id, assistant_id);
create index idx_document_chunks_embedding on document_chunks using hnsw (embedding vector_cosine_ops);

create table audit_logs (
    id uuid primary key,
    created_at timestamptz not null,
    updated_at timestamptz not null,
    tenant_id uuid not null references tenants(id),
    assistant_id uuid not null,
    user_id uuid not null,
    prompt text not null,
    answer text not null,
    model_name varchar(120) not null,
    retrieved_chunks integer not null,
    latency_ms bigint not null
);

create index idx_audit_logs_tenant_created on audit_logs(tenant_id, created_at desc);
