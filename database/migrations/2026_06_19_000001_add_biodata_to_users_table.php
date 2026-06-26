<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::table('users', function (Blueprint $table) {
            $table->string('panggilan')->nullable()->after('name');
            $table->date('tanggal_lahir')->nullable()->after('panggilan');
            $table->text('alamat')->nullable()->after('tanggal_lahir');
        });
    }

    public function down(): void
    {
        Schema::table('users', function (Blueprint $table) {
            $table->dropColumn(['panggilan', 'tanggal_lahir', 'alamat']);
        });
    }
};
